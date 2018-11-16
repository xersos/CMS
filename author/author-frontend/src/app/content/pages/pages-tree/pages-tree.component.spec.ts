import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PagesTreeComponent } from './pages-tree.component';

describe('PagesTreeComponent', () => {
  let component: PagesTreeComponent;
  let fixture: ComponentFixture<PagesTreeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PagesTreeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PagesTreeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
