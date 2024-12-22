import { LocalDate } from 'domain/datetime';
import { MyEnum } from 'generated/io/github/kt2tssample/Enum.generated';
import { MySampleId } from 'generated/io/github/kt2tssample/SampleId.generated';
import { BaseDataClass } from 'generated/io/github/kt2tssample/subpackage/SubPackageClasses.generated';
import { Dict } from 'utils/nominal-class';

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
  enum: MyEnum;
  nullableEnum?: MyEnum;
  intList: number[];
  nullableIntList: (number | null)[];
  stringList: string[];
  nullableStringList: (string | null)[];
  enumList: MyEnum[];
  nullableEnumList: (MyEnum | null)[];
  classList: BaseDataClass[];
  nullableClassList: (BaseDataClass | null)[];
  nullableClassNullableList?: (BaseDataClass | null)[];
  nullableList?: BaseDataClass[];
  set: BaseDataClass[];
  nullableClassSet: (BaseDataClass | null)[];
  nullableClassNullableSet?: (BaseDataClass | null)[];
  nullableSet?: BaseDataClass[];
  mapEnumKey: Dict<MyEnum, BaseDataClass>;
  mapIdKey: Dict<MySampleId, BaseDataClass>;
  map: Dict<string, BaseDataClass>;
  nullableClassMap: Dict<string, BaseDataClass | null>;
  nullableClassNullableMap?: Dict<string, BaseDataClass | null>;
  nullableMap?: Dict<string, BaseDataClass>;
  date: LocalDate;
  nullableDate?: LocalDate;
  item: BaseDataClass;
  nullableItem?: BaseDataClass;
  id: MySampleId;
  nullableId?: MySampleId;
  intPair: [number, number];
  nullableIntPair: [number | null, number | null];
  stringPair: [string, string];
  nullableStringPair: [string | null, string | null];
  classPair: [BaseDataClass, BaseDataClass];
  nullableClassPair: [BaseDataClass | null, BaseDataClass | null];
  nullableClassNullablePair?: [BaseDataClass | null, BaseDataClass | null];
  nullablePair?: [BaseDataClass, BaseDataClass];
}

export interface ComplexStuff {
  value: [Dict<MySampleId, BaseDataClass>, BaseDataClass][][];
}
