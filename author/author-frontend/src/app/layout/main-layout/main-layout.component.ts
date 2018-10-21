import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {BreadCrumbItem} from '../../shared/models/bread.crumb.item';
import {ActivatedRoute, ActivationEnd, Router} from '@angular/router';
import {BaseComponent} from '../../base.component';
import {takeUntil} from 'rxjs/operators';

@Component({
  selector: 'zcms-main-layout',
  templateUrl: './main-layout.component.html',
  styleUrls: ['./main-layout.component.scss']
})
export class MainLayoutComponent extends BaseComponent implements OnInit {

  public breadCumbs: BreadCrumbItem[] = [];
  public addUrl: string = null;
  public addText: string = null;

  constructor(private route: ActivatedRoute, private router: Router, private changeDetector: ChangeDetectorRef) {
    super();

    this.router.events.pipe(takeUntil(this.ngUnsubscribe))
      .subscribe(event => {
        if (event instanceof ActivationEnd && event.snapshot.routeConfig.path !== '') {
          this.breadCumbs = event.snapshot.data.breadCrumbs;
          this.addUrl = event.snapshot.data.addUrl;
          this.addText = event.snapshot.data.addText;
          this.changeDetector.detectChanges();
        }
      });
  }

  ngOnInit() {
  }

}
