
export interface PackageDeclaration {
  name: string;
}

export interface ClassSimpleName {
  name: string;
}

export interface ClassQualifiedName {
  name: string;
}

export interface TypeDeclaration {
  name: ClassSimpleName;
  generics: TypeDeclaration[];
}

export interface LocalClassDeclaration {
  localName: ClassSimpleName;
  qualifiedName: ClassQualifiedName;
}

export interface FieldDeclaration {
  fieldName: string;
  type: TypeDeclaration;
}

export interface ClassAnnotation {
  annotation: ClassSimpleName;
  values: Map<string, string>;
}

export type SelectionOrigin =;

export interface ClassDeclaration {
  localName: ClassSimpleName;
  annotations: ClassAnnotation[];
  isSealed: boolean;
  sealedChildClasses: ClassSimpleName[];
  parentClasses: ClassSimpleName[];
  fields: FieldDeclaration[];
  selectionOrigins: SelectionOrigin[];
}
