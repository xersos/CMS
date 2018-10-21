import {EventEmitter} from '@angular/core';

export class EmitterService {

  private static emitters: { [ID: string]: EventEmitter<any> } = {};

  public static of(ID: string): EventEmitter<any> {
    if (!this.emitters[ID]) {
      this.emitters[ID] = new EventEmitter<any>();
    }

    return this.emitters[ID];
  }
}
