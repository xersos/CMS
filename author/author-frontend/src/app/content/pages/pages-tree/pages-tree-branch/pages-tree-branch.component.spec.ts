import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PagesTreeBranchComponent } from './pages-tree-branch.component';

describe('PagesTreeBranchComponent', () => {
  let component: PagesTreeBranchComponent;
  let fixture: ComponentFixture<PagesTreeBranchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PagesTreeBranchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PagesTreeBranchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
