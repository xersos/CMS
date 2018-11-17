import { Component, OnInit } from '@angular/core';
import {FlashMessageService} from '../../service/flash-message.service';
import {FlashMessage} from '../../models/flash.message';

@Component({
  selector: 'zcms-flash-message',
  templateUrl: './flash-message.component.html',
  styleUrls: ['./flash-message.component.scss']
})
export class FlashMessageComponent implements OnInit {

  public message: FlashMessage = null;

  constructor(private flashMessageService: FlashMessageService) { }

  ngOnInit() {
    this.message = this.flashMessageService.getMessage();
  }

}
