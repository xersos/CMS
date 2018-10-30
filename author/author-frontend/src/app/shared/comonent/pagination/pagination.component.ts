import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'zcms-pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.scss']
})
export class PaginationComponent implements OnInit {

  @Input() totalPages: number;
  @Input() pages: number[];
  @Input() currentPage: number;
  @Input() class: string = '';

  @Output() pageChange: EventEmitter<number> = new EventEmitter<number>();

  private lastEmitted: number = -1;

  constructor() { }

  ngOnInit() {
  }

  public changePage(page: number): boolean {
    if (page !== this.lastEmitted) {
      this.pageChange.emit(page);
      this.lastEmitted = page;
    }
    return false;
  }

}
