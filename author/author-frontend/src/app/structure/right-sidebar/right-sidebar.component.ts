import {Component, ElementRef, Input, OnInit} from '@angular/core';
import {Replace} from '../../shared/utils/functions';
import {asideMenuCssClasses} from '../structure.directives';

@Component({
  selector: 'zcms-right-sidebar',
  templateUrl: './right-sidebar.component.html',
  styleUrls: ['./right-sidebar.component.scss']
})
export class RightSidebarComponent implements OnInit {

  @Input() display: any;
  @Input() fixed: boolean;

  constructor(private el: ElementRef) { }

  ngOnInit() {
    Replace(this.el);

    if (this.fixed) {
      document.querySelector('body').classList.add('aside-menu-fixed');
    }

    if (this.display !== false) {
      let cssClass;
      this.display ? cssClass = `aside-menu-${this.display}-show` : cssClass = asideMenuCssClasses[0];
      document.querySelector('body').classList.add(cssClass);
    }
  }

}
