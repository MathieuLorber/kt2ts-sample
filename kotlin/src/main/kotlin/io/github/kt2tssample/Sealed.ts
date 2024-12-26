
export type SomeSealedClass = | SomeClassImpl | AnotherClassImpl;

export interface SomeClassImpl {
  objectType: "SomeClassImpl";
  someValue: string;
}

export interface AnotherClassImpl {
  objectType: "AnotherClassImpl";
  anotherValue: number;
}
