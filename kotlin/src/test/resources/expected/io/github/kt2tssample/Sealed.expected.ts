export type SomeSealedClass = AnotherClassImpl | SomeClassImpl;

export interface SomeClassImpl {
  objectType: 'SomeClassImpl';
  someValue: string;
}

export interface AnotherClassImpl {
  objectType: 'AnotherClassImpl';
  anotherValue: number;
}
