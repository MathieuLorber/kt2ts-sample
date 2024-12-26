
export type SomeSealedClass = | WithList | ComplexGenerics | SomeClassImpl | AnotherClassImpl;

export interface WithList {
  objectType: "WithList";
  list: List<string>;
}

export interface ComplexGenerics {
  objectType: "ComplexGenerics";
  list: List<Pair<string, string>>;
}

export interface SomeClassImpl {
  objectType: "SomeClassImpl";
  someValue: string;
}

export interface AnotherClassImpl {
  objectType: "AnotherClassImpl";
  anotherValue: number;
}
