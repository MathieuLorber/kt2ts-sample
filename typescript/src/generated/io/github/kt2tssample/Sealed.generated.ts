export type SomeSealedClass =
  | AnotherClassImpl
  | ComplexGenerics
  | SomeClassImpl
  | WithList;

export interface WithList {
  objectType: 'WithList';
  list: string[];
}

export interface ComplexGenerics {
  objectType: 'ComplexGenerics';
  list: [string, string][];
}

export interface SomeClassImpl {
  objectType: 'SomeClassImpl';
  someValue: string;
}

export interface AnotherClassImpl {
  objectType: 'AnotherClassImpl';
  anotherValue: number;
}
