import {OnDestroy} from '@angular/core';
import {Subject} from 'rxjs';
import {AbstractControl, FormGroup} from '@angular/forms';

export abstract class BaseComponent implements OnDestroy {

  protected ngUnsubscribe: Subject<boolean> = new Subject<boolean>();

  ngOnDestroy() {
    this.ngUnsubscribe.next(true);
    this.ngUnsubscribe.complete();
  }

  protected onFormValueChanged(form: FormGroup, formErrors: any, validationMessages: any): any {
    for (const field in formErrors) {
      formErrors[field] = ''; // Clear all form errors
      const control: AbstractControl = form.get(field);

      if (control && control.dirty && !control.valid) {
        const messages = validationMessages[field];
        for (const key in control.errors) {
          if (messages[key]) {
            formErrors[field] = messages[key];
            break;
          }
        }
      }
    }
  }
}
