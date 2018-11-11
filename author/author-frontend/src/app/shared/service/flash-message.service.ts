import { Injectable } from '@angular/core';
import {FlashMessage} from '../models/flash.message';

@Injectable({
  providedIn: 'root'
})
export class FlashMessageService {

  private currentMessage: FlashMessage = null;

  public setMessage(message: FlashMessage): void {
    this.currentMessage = message;
  }

  public getMessage(): FlashMessage {
    const msg = this.currentMessage;
    this.currentMessage = null;
    return msg;
  }
}
