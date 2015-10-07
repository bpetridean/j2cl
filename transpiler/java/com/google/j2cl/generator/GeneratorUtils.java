/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.j2cl.generator;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.j2cl.ast.BinaryOperator;
import com.google.j2cl.ast.Expression;
import com.google.j2cl.ast.Field;
import com.google.j2cl.ast.JavaType;
import com.google.j2cl.ast.MemberReference;
import com.google.j2cl.ast.Method;
import com.google.j2cl.ast.PrefixOperator;
import com.google.j2cl.ast.TypeDescriptor;
import com.google.j2cl.ast.Variable;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.List;

/**
 * Utility functions related to source generation in the J2CL AST.
 */
public class GeneratorUtils {
  public static String getSourceName(TypeDescriptor typeDescriptor) {
    return typeDescriptor.getRawTypeDescriptor().getSourceName().replace('$', '.');
  }

  public static String getBinaryName(TypeDescriptor typeDescriptor) {
    return typeDescriptor.getBinaryName();
  }

  /**
   * Returns the relative output path for a given type.
   */
  public static String getRelativePath(JavaType javaType) {
    TypeDescriptor descriptor = javaType.getDescriptor();
    String typeName = descriptor.getClassName();
    String packageName = descriptor.getPackageName();
    return packageName.replace('.', '/') + "/" + typeName;
  }

  /**
   * Returns the absolute path for the given output FileSystem and output paths.
   */
  public static Path getAbsolutePath(
      FileSystem outputFileSystem,
      String outputLocationPath,
      String relativeFilePath,
      String suffix) {
    return outputLocationPath != null
        ? outputFileSystem.getPath(outputLocationPath, relativeFilePath + suffix)
        : outputFileSystem.getPath(relativeFilePath + suffix);
  }

  public static String getParameterList(
      Method method, final StatementSourceGenerator statementSourceGenerator) {
    List<String> parameterNameList =
        Lists.transform(
            method.getParameters(),
            new Function<Variable, String>() {
              @Override
              public String apply(Variable variable) {
                return statementSourceGenerator.toSource(variable);
              }
            });
    return Joiner.on(", ").join(parameterNameList);
  }

  public static boolean isVoid(TypeDescriptor typeDescriptor) {
    return typeDescriptor.getClassName().equals("void");
  }

  /**
   * Returns whether the $clinit function should be rewritten as NOP.
   */
  public static boolean needRewriteClinit(JavaType type) {
    for (Field staticField : type.getStaticFields()) {
      if (staticField.hasInitializer()) {
        return true;
      }
    }
    return !type.getStaticInitializerBlocks().isEmpty();
  }

  /**
   * Return the String with first letter capitalized.
   */
  public static String toProperCase(String string) {
    if (string.isEmpty()) {
      return string;
    } else if (string.length() == 1) {
      return string.toUpperCase();
    }
    return string.substring(0, 1).toUpperCase() + string.substring(1, string.length());
  }

  private GeneratorUtils() {}

  public static String getArrayAssignmentFunctionName(BinaryOperator binaryOperator) {
    switch (binaryOperator) {
      case ASSIGN:
        return "$set";
      case PLUS_ASSIGN:
        return "$addSet";
      case MINUS_ASSIGN:
        return "$subSet";
      case TIMES_ASSIGN:
        return "$mulSet";
      case DIVIDE_ASSIGN:
        return "$divSet";
      case BIT_AND_ASSIGN:
        return "$andSet";
      case BIT_OR_ASSIGN:
        return "$orSet";
      case BIT_XOR_ASSIGN:
        return "$xorSet";
      case REMAINDER_ASSIGN:
        return "$modSet";
      case LEFT_SHIFT_ASSIGN:
        return "$lshiftSet";
      case RIGHT_SHIFT_SIGNED_ASSIGN:
        return "$rshiftSet";
      case RIGHT_SHIFT_UNSIGNED_ASSIGN:
        return "$rshiftUSet";
      default:
        Preconditions.checkState(
            false, "Requested the Arrays function name for a non-assignment operator.");
        return null;
    }
  }

  public static boolean isValidForLongs(PrefixOperator prefixOperator) {
    return prefixOperator != PrefixOperator.NOT;
  }

  public static String getLongOperationFunctionName(PrefixOperator prefixOperator) {
    switch (prefixOperator) {
      case PLUS:
        Preconditions.checkArgument(false, "The '+' prefix operator is a NOP for longs.");
        return null;
      case MINUS:
        return "$negate"; // Multiply by -1;
      case COMPLEMENT:
        return "$not"; // Bitwise not
      case NOT:
        Preconditions.checkArgument(false, "The 'not' operator isn't applicable for longs.");
        return null;
      default:
        Preconditions.checkArgument(
            false,
            "Requested the Longs function name for unrecognized prefix operator "
                + prefixOperator
                + ".");
        return null;
    }
  }

  public static String getLongOperationFunctionName(BinaryOperator binaryOperator) {
    switch (binaryOperator) {
      case TIMES:
        return "$times";
      case DIVIDE:
        return "$divide";
      case REMAINDER:
        return "$remainder";
      case PLUS:
        return "$plus";
      case MINUS:
        return "$minus";
      case LEFT_SHIFT:
        return "$leftShift";
      case RIGHT_SHIFT_SIGNED:
        return "$rightShiftSigned";
      case RIGHT_SHIFT_UNSIGNED:
        return "$rightShiftUnsigned";
      case LESS:
        return "$less";
      case GREATER:
        return "$greater";
      case LESS_EQUALS:
        return "$lessEquals";
      case GREATER_EQUALS:
        return "$greaterEquals";
      case EQUALS:
        return "$equals";
      case NOT_EQUALS:
        return "$notEquals";
      case XOR:
        return "$xor";
      case AND:
        return "$and";
      case OR:
        return "$or";
      case CONDITIONAL_AND:
        Preconditions.checkArgument(false, "The '&&' operator isn't applicable for longs.");
        return null;
      case CONDITIONAL_OR:
        Preconditions.checkArgument(false, "The '||' operator isn't applicable for longs.");
        return null;
      case ASSIGN:
        Preconditions.checkArgument(
            false, "The '=' operator doesn't require special handling for longs.");
        return null;
      default:
        Preconditions.checkArgument(
            false,
            "Requested the Longs function name for unrecognized binary operator "
                + binaryOperator
                + ".");
        return null;
    }
  }

  /**
   * If possible, returns the qualifier of the provided expression, otherwise null.
   */
  public static Expression getQualifier(Expression expression) {
    if (!(expression instanceof MemberReference)) {
      return null;
    }
    return ((MemberReference) expression).getQualifier();
  }

  public static Expression getInitialValue(Field field) {
    if (field.isCompileTimeConstant()) {
      return field.getInitializer();
    }
    return field.getDescriptor().getTypeDescriptor().getDefaultValue();
  }

  public static boolean hasJsDoc(JavaType type) {
    return !type.getSuperInterfaceTypeDescriptors().isEmpty()
        || type.getDescriptor().isParameterizedType()
        || (type.getSuperTypeDescriptor() != null
            && type.getSuperTypeDescriptor().isParameterizedType());
  }
}