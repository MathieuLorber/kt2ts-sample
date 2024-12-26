export type SomeSealedClass =
  | WithList
  | ComplexGenerics
  | SomeClassImpl
  | AnotherClassImpl;

export interface WithList {
  objectType: 'WithList';
  list: string[];
}

export interface ComplexGenerics {
  objectType: 'ComplexGenerics';
  list: Pair<string, string>[];
}

export interface SomeClassImpl {
  objectType: 'SomeClassImpl';
  someValue: string;
}

export interface AnotherClassImpl {
  objectType: 'AnotherClassImpl';
  anotherValue: number;
}
