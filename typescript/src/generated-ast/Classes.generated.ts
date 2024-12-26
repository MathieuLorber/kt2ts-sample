export interface MyDataClass {
  boolean: boolean;
  nullableBoolean: boolean;
  double: Double;
  nullableDouble: Double;
  int: number;
  nullableInt: number;
  long: Long;
  nullableLong: Long;
  string: string;
  nullableString: string;
  enum: MyEnum;
  nullableEnum: MyEnum;
  intList: number[];
  nullableIntList: number[];
  stringList: string[];
  nullableStringList: string[];
  enumList: MyEnum[];
  nullableEnumList: MyEnum[];
  classList: BaseDataClass[];
  nullableClassList: BaseDataClass[];
  nullableClassNullableList: BaseDataClass[];
  nullableList: BaseDataClass[];
  set: BaseDataClass[];
  nullableClassSet: BaseDataClass[];
  nullableClassNullableSet: BaseDataClass[];
  nullableSet: BaseDataClass[];
  mapEnumKey: Map<MyEnum, BaseDataClass>;
  mapIdKey: Map<MySampleId, BaseDataClass>;
  map: Map<string, BaseDataClass>;
  nullableClassMap: Map<string, BaseDataClass>;
  nullableClassNullableMap: Map<string, BaseDataClass>;
  nullableMap: Map<string, BaseDataClass>;
  date: LocalDate;
  nullableDate: LocalDate;
  item: BaseDataClass;
  nullableItem: BaseDataClass;
  id: MySampleId;
  nullableId: MySampleId;
  intPair: Pair<number, number>;
  nullableIntPair: Pair<number, number>;
  stringPair: Pair<string, string>;
  nullableStringPair: Pair<string, string>;
  classPair: Pair<BaseDataClass, BaseDataClass>;
  nullableClassPair: Pair<BaseDataClass, BaseDataClass>;
  nullableClassNullablePair: Pair<BaseDataClass, BaseDataClass>;
  nullablePair: Pair<BaseDataClass, BaseDataClass>;
  any: Any;
  nullableAny: Any;
}

export interface ComplexStuff {
  value: Pair<
    Map<MySampleId, Map<MySampleId, BaseDataClass>[]>,
    BaseDataClass
  >[][];
}
