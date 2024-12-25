export type SomeSealedClass = AnotherClassImpl | SomeClassImpl;

export interface SomeClassImpl {
  objectType: 'SomeClassImpl';
  someValue: string;
  someValue1: string;
  someValue2: string;
  someValue3: string;
  someValue4: string;
  someValue5: string;
  someValue6: string;
  someValue7: string;
  someValue8: string;
  someValue9: string;
}

export interface AnotherClassImpl {
  objectType: 'AnotherClassImpl';
  anotherValue: number;
}
