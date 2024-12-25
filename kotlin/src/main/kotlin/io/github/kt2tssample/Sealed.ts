
export type SomeSealedClass = | SomeClassImpl | AnotherClassImpl;

export interface SomeClassImpl {
  query: "SomeClassImpl";
  someValue: String;
}

export interface AnotherClassImpl {
  query: "AnotherClassImpl";
  anotherValue: Int;
}
