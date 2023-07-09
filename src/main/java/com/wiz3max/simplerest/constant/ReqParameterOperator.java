package com.wiz3max.simplerest.constant;

import java.util.function.BiPredicate;


public enum ReqParameterOperator {
    lt((o1, o2)-> o1.compareTo(o2) == -1),
    lte((o1, o2)-> o1.compareTo(o2) <= 0),
    gt((o1, o2)-> o1.compareTo(o2) == 1),
    gte((o1, o2)-> o1.compareTo(o2) >= 0),
    eq((o1, o2)-> o1.compareTo(o2) == 0),
    ne((o1, o2)-> o1.compareTo(o2) != 0);

    public static final ReqParameterOperator DEFAULT = eq;

    private BiPredicate<? super Comparable, ? super Comparable> fn;

    ReqParameterOperator(BiPredicate<? super Comparable, ? super Comparable> fn){
        this.fn = fn;
    }

    public BiPredicate<? super Comparable, ? super Comparable> getOperatorFunction(){
        return this.fn;
    }

}
