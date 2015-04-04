// Compiled by ClojureScript 0.0-2850 {}
goog.provide('cljs.fuzzy.fzlogic');
goog.require('cljs.core');
/**
* @param {...*} var_args
*/
cljs.fuzzy.fzlogic.and = (function() { 
var and__delegate = function (x,next){
return cljs.core.apply.call(null,cljs.core.min,x,next);
};
var and = function (x,var_args){
var next = null;
if (arguments.length > 1) {
var G__29945__i = 0, G__29945__a = new Array(arguments.length -  1);
while (G__29945__i < G__29945__a.length) {G__29945__a[G__29945__i] = arguments[G__29945__i + 1]; ++G__29945__i;}
  next = new cljs.core.IndexedSeq(G__29945__a,0);
} 
return and__delegate.call(this,x,next);};
and.cljs$lang$maxFixedArity = 1;
and.cljs$lang$applyTo = (function (arglist__29946){
var x = cljs.core.first(arglist__29946);
var next = cljs.core.rest(arglist__29946);
return and__delegate(x,next);
});
and.cljs$core$IFn$_invoke$arity$variadic = and__delegate;
return and;
})()
;
/**
* @param {...*} var_args
*/
cljs.fuzzy.fzlogic.or = (function() { 
var or__delegate = function (x,next){
return cljs.core.apply.call(null,cljs.core.max,x,next);
};
var or = function (x,var_args){
var next = null;
if (arguments.length > 1) {
var G__29947__i = 0, G__29947__a = new Array(arguments.length -  1);
while (G__29947__i < G__29947__a.length) {G__29947__a[G__29947__i] = arguments[G__29947__i + 1]; ++G__29947__i;}
  next = new cljs.core.IndexedSeq(G__29947__a,0);
} 
return or__delegate.call(this,x,next);};
or.cljs$lang$maxFixedArity = 1;
or.cljs$lang$applyTo = (function (arglist__29948){
var x = cljs.core.first(arglist__29948);
var next = cljs.core.rest(arglist__29948);
return or__delegate(x,next);
});
or.cljs$core$IFn$_invoke$arity$variadic = or__delegate;
return or;
})()
;

//# sourceMappingURL=fzlogic.js.map?rel=1428071057221