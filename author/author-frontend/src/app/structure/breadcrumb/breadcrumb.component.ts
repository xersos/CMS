import {Component, Input, OnInit} from '@angular/core';
import {BreadCrumbItem} from '../../shared/models/bread.crumb.item';

@Component({
  selector: 'zcms-breadcrumb',
  templateUrl: './breadcrumb.component.html',
  styleUrls: ['./breadcrumb.component.scss']
})
export class BreadcrumbComponent implements OnInit {

  @Input() public breadcrumbs: BreadCrumbItem[];

  @Input() public addUrl: string;

  @Input() public addText: string;

  constructor() { }

  ngOnInit() {
  }

}
