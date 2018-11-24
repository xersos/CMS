import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddUpdatePageComponent } from './add-update-page.component';

describe('AddUpdatePageComponent', () => {
  let component: AddUpdatePageComponent;
  let fixture: ComponentFixture<AddUpdatePageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddUpdatePageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddUpdatePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
