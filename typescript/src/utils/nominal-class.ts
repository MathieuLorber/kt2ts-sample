export type NominalItem = NominalString<any> | NominalNumber<any>;

export type NominalString<T extends string> = string &
  TypeGuardedNominalString<T>;

abstract class TypeGuardedNominalString<T extends string> {
  private _typeGuard!: T;
}

export type NominalNumber<T extends string> = number &
  TypeGuardedNominalNumber<T>;

abstract class TypeGuardedNominalNumber<T extends string> {
  private _typeGuard!: T;
}

type DictKey = NominalItem | string;

export class Dict<K extends DictKey, T> {
  private _typeGuardKey!: K;
  private _typeGuardValue!: T;
}
