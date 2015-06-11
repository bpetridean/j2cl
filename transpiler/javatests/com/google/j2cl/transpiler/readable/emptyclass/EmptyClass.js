goog.module('gen.com.google.j2cl.transpiler.readable.emptyclass.EmptyClassModule');


var Class = goog.require('gen.java.lang.CoreModule').Class;
var Object = goog.require('gen.java.lang.CoreModule').Object;
var Util = goog.require('nativebootstrap.UtilModule').Util;


/**
 * Transpiled from
 * third_party/java_src/j2cl/transpiler/javatests/com/google/j2cl/transpiler/readable/emptyclass/EmptyClass.java.
 */
class EmptyClass extends Object {
  /**
   * Defines instance fields.
   * @private
   */
  constructor() { super(); }

  /**
   * Runs inline instance field initializers.
   * @param {EmptyClass} instance
   * @private
   */
  static $init(instance) {}

  /**
   * A particular Java constructor as a factory method.
   * @return {!EmptyClass}
   * @public
   */
  static $create() {
    EmptyClass.$clinit();
    var instance = new EmptyClass;
    EmptyClass.$ctor__com_google_j2cl_transpiler_readable_emptyclass_EmptyClass(
        instance);
    return instance;
  }

  /**
   * Initializes instance fields for a particular Java constructor.
   * @param {EmptyClass} instance
   * @protected
   */
  static $ctor__com_google_j2cl_transpiler_readable_emptyclass_EmptyClass(
      instance) {
    Object.$ctor__java_lang_Object(instance);
    EmptyClass.$init(instance);
  }

  /**
   * Returns whether the provided instance is an instance of this class.
   * @return {boolean}
   * @public
   */
  static $isInstance(instance) { return instance instanceof EmptyClass; }

  /**
   * Returns whether the provided class is or extends this class.
   * @param {Function} classConstructor
   * @return {boolean}
   * @private
   */
  static $isAssignableFrom(classConstructor) {
    return Util.$canCastClass(classConstructor, EmptyClass);
  }

  /**
   * Runs inline static field initializers.
   * @protected
   */
  static $clinit() { Object.$clinit(); }
};


/**
 * @public {Class}
 */
EmptyClass.$class = Class.$createForClass(
    Util.$generateId('EmptyClass'),
    Util.$generateId(
        'com.google.j2cl.transpiler.readable.emptyclass.EmptyClass'),
    Object.$class,
    Util.$generateId(
        'com.google.j2cl.transpiler.readable.emptyclass.EmptyClass'));


/**
 * Exported class.
 */
exports.EmptyClass = EmptyClass;
