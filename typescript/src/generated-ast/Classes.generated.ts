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
  mapEnumKey: Map<MyEnum, BaseDataClass>;
  mapIdKey: Map<MySampleId, BaseDataClass>;
  map: Map<string, BaseDataClass>;
  nullableClassMap: Map<string, BaseDataClass>;
  nullableClassNullableMap?: Map<string, BaseDataClass>;
  nullableMap?: Map<string, BaseDataClass>;
  date: LocalDate;
  nullableDate?: LocalDate;
  item: BaseDataClass;
  nullableItem?: BaseDataClass;
  id: MySampleId;
  nullableId?: MySampleId;
  intPair: Pair<number, number>;
  nullableIntPair: Pair<number, number>;
  stringPair: Pair<string, string>;
  nullableStringPair: Pair<string, string>;
  classPair: Pair<BaseDataClass, BaseDataClass>;
  nullableClassPair: Pair<BaseDataClass, BaseDataClass>;
  nullableClassNullablePair?: Pair<BaseDataClass, BaseDataClass>;
  nullablePair?: Pair<BaseDataClass, BaseDataClass>;
  any: Any;
  nullableAny?: Any;
}

export interface ComplexStuff {
  value: Pair<
    Map<MySampleId, Map<MySampleId, BaseDataClass>[]>,
    BaseDataClass
  >[][];
}
