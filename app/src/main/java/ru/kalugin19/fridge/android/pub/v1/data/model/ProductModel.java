package ru.kalugin19.fridge.android.pub.v1.data.model;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import ru.kalugin19.fridge.android.pub.v1.FridgeApplication;
import ru.kalugin19.fridge.android.pub.v1.data.entity.Member;
import ru.kalugin19.fridge.android.pub.v1.data.entity.Product;
import ru.kalugin19.fridge.android.pub.v1.data.entity.TypeProducts;
import ru.kalugin19.fridge.android.pub.v1.data.remote.ConnectionService;
import ru.kalugin19.fridge.android.pub.v1.data.remote.FirebaseService;
import ru.kalugin19.fridge.android.pub.v1.injection.qualifiers.ApplicationContext;
import ru.kalugin19.fridge.android.pub.v1.ui.base.util.Constants;


/**
 * Модель продукт
 */
public class ProductModel {

    private final String END_SYMBOL = "\uf8ff";
    private final FirebaseService firebaseService;
    private final int THREE_DAYS = 3;
    private final ConnectionService connectionService;

    @Inject
    ProductModel(@ApplicationContext Context context, FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
        this.connectionService = ((FridgeApplication) context).getComponent().connectionService();
    }

    public Observable<Boolean> addProduct(final Product product) {
        return Observable.create(emitter -> firebaseService.addProduct(product).addOnCompleteListener(task -> emitter.onNext(task.isSuccessful())).addOnFailureListener(e -> emitter.onError(e)));
    }

    public Observable<Boolean> editProduct(final Product product) {
        return Observable.create((ObservableOnSubscribe<Boolean>) emitter -> firebaseService.editProduct(product).addOnCompleteListener(task -> emitter.onNext(task.isSuccessful())).addOnFailureListener(emitter::onError)).subscribeOn(Schedulers.io());
    }

    public Observable<List<Product>> getProducts(final TypeProducts typeProducts, final String searchText) {
        return Observable.create(new ObservableOnSubscribe<List<Product>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Product>> emmiter) throws Exception {
                if (!connectionService.checkInternetConnection()) {
                    emmiter.onError(new NoInternetException());
                } else {
                    final List<Product> products = new ArrayList<>();
                    final ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                for (DataSnapshot member : snapshot.child(Constants.INSTANCE.getMEMBERS()).getChildren()) {
                                    String uid = member.getKey();
                                    Member user = member.getValue(Member.class);
                                    if (uid.equals(firebaseService.getmAuth().getCurrentUser().getUid())) {

                                        for (DataSnapshot dsp : snapshot.child(Constants.INSTANCE.getPRODUCTS()).getChildren()) {
                                            Product product = dsp.getValue(Product.class);
                                            if (product != null) {
                                                if (user != null) {
                                                    Member userOwner = snapshot.child(Constants.INSTANCE.getOWNER()).getValue(Member.class);
                                                    product.setTypeMember(user.getType());
                                                    product.setOwnerEmail(userOwner.getEmail());
                                                }
                                            }
                                            if (product != null) {
                                                product.setKey(dsp.getKey());
                                                if (typeProducts == TypeProducts.FRESH) {
                                                    if ((product.getSpoiledDate() - Calendar.getInstance().getTimeInMillis()) > 0) {
                                                        long currentPeriod = product.getSpoiledDate() - Calendar.getInstance().getTimeInMillis();
                                                        if (TimeUnit.MILLISECONDS.toDays(currentPeriod) > THREE_DAYS) {
                                                            products.add(product);
                                                        }
                                                    }
                                                } else if (typeProducts == TypeProducts.SPOILED) {
                                                    if (product.getSpoiledDate() - Calendar.getInstance().getTimeInMillis() < 0) {
                                                        products.add(product);
                                                    }
                                                } else if (typeProducts == TypeProducts.SOON) {
                                                    if ((product.getSpoiledDate() - Calendar.getInstance().getTimeInMillis()) > 0) {
                                                        long currentPeriod = product.getSpoiledDate() - Calendar.getInstance().getTimeInMillis();
                                                        if (TimeUnit.MILLISECONDS.toDays(currentPeriod) <= THREE_DAYS) {
                                                            products.add(product);
                                                        }
                                                    }
                                                } else {
                                                    products.add(product);
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                            firebaseService.getFridgesReferences().removeEventListener(this);
                            Collections.reverse(products);
                            emmiter.onNext(products);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            firebaseService.getFridgesReferences().removeEventListener(this);
                            emmiter.onError(databaseError.toException());
                        }
                    };

                    if (searchText != null && searchText.length() > 0) {
                        firebaseService.getFridgesReferences().addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        firebaseService.getDatabase().getReferenceFromUrl(firebaseService.getmFirebaseReference().toString())
                                .orderByChild(Constants.INSTANCE.getPRODUCTS_FIELD_UPPER_NAME()).startAt(searchText.toUpperCase()).endAt(searchText.toUpperCase() + END_SYMBOL).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot item : dataSnapshot.getChildren()) {
                                    Product product = item.getValue(Product.class);
                                    if (product != null) {
                                        product.setKey(product.getKey());
                                    }
                                    products.add(product);
                                }
                                emmiter.onNext(products);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                emmiter.onError(databaseError.toException());
                            }
                        });
                    } else {
                        firebaseService.getFridgesReferences().addValueEventListener(eventListener);
                    }
                }
            }
        })
                .subscribeOn(Schedulers.io());
    }

    public Observable<String> deleteProducts(final List<Product> products) {
        return Observable.create((ObservableOnSubscribe<String>) emitter -> {
            for (final Product product : products) {
                if (product != null && product.getKey() != null) {
                    firebaseService.deletedProduct(product).addOnCompleteListener(task -> emitter.onNext(product.getPhotoName())).addOnFailureListener(emitter::onError);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    public Observable<Void> addProducts(final List<Product> products) {
        return Observable.create((ObservableOnSubscribe<Void>) emmiter -> {
            for (final Product product : products) {
                firebaseService.addProduct(product)
                        .addOnFailureListener(emmiter::onError);
            }
            emmiter.onComplete();
        }).subscribeOn(Schedulers.io());
    }
}
