import { LocalDate } from 'domain/datetime';
import { BaseDataClass } from 'generated/io/github/kt2tssample/subpackage/SubPackageClasses.generated';

export interface MyDataClass {
  boolean: boolean;
  nullableBoolean?: boolean;
  double: number;
  nullableDouble?: number;
  int: number;
  nullableInt?: number;
  long: number;
  nullableLong?: number;
  string: string;
  nullableString?: string;
  intList: number[];
  nullableIntList: (number | null)[];
  stringList: string[];
  nullableStringList: (string | null)[];
  classList: BaseDataClass[];
  nullableClassList: (BaseDataClass | null)[];
  nullableClassNullableList?: (BaseDataClass | null)[];
  nullableList?: BaseDataClass[];
  set: BaseDataClass[];
  nullableClassSet: (BaseDataClass | null)[];
  nullableClassNullableSet?: (BaseDataClass | null)[];
  nullableSet?: BaseDataClass[];
  map: any;
  nullableClassMap: any;
  nullableClassNullableMap?: any;
  nullableMap?: any;
  date: LocalDate;
  nullableDate?: LocalDate;
  item: BaseDataClass;
  nullableItem?: BaseDataClass;
  intPair: [number, number];
  nullableIntPair: [number | null, number | null];
  stringPair: [string, string];
  nullableStringPair: [string | null, string | null];
  classPair: [BaseDataClass, BaseDataClass];
  nullableClassPair: [BaseDataClass | null, BaseDataClass | null];
  nullableClassNullablePair?: [BaseDataClass | null, BaseDataClass | null];
  nullablePair?: [BaseDataClass, BaseDataClass];
}