export class FlashMessage {
  public type: FlashMessageType;
  public message: string;


  constructor(type: FlashMessageType, message: string) {
    this.type = type;
    this.message = message;
  }
}

export enum FlashMessageType {
  DANGER = 'danger',
  INFO = 'info',
  SUCCESS = 'success'
}
