import {Component, ElementRef, Input, OnInit} from '@angular/core';
import {Replace} from '../../shared/utils/functions';

@Component({
  selector: 'zcms-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {

  @Input() fixed: boolean;

  constructor(private el: ElementRef) { }

  ngOnInit() {
    Replace(this.el);

    if (this.fixed) {
      document.querySelector('body').classList.add('footer-fixed');
    }
  }

}
