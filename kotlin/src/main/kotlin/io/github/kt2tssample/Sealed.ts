export type SomeSealedClass = | SomeClassImpl | AnotherClassImpl;
export interface SomeClassImpl {
  someValue: String;
}
export interface AnotherClassImpl {
  anotherValue: Int;
}
