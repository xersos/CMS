import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {BsModalRef} from 'ngx-bootstrap';

@Component({
  selector: 'zcms-confirm-modal',
  templateUrl: './confirm-modal.component.html',
  styleUrls: ['./confirm-modal.component.scss']
})
export class ConfirmModalComponent implements OnInit {

  @Output() confirmed: EventEmitter<boolean> = new EventEmitter<boolean>();

  public title: string = '';
  public body: string = '';
  public closeBtn: string = '';
  public okBtn: string = '';

  constructor(private bsModalRef: BsModalRef) {}

  ngOnInit() {
  }

  public ok(): void {
    this.confirmed.emit(true);
    this.bsModalRef.hide();
  }

  public close(): void {
    this.confirmed.emit(false);
    this.bsModalRef.hide();
  }

}
