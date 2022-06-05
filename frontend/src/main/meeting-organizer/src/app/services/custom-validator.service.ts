import {AbstractControl, FormGroup, ValidatorFn} from '@angular/forms';
import {Moment} from 'moment';
import * as moment from 'moment';

export class CustomValidator {
  static passwordMatchValidator(password: string, passwordConfirm: string): (formGroup: FormGroup) => void {
    return (formGroup: FormGroup) => {
      const control = formGroup.controls[password];
      const matchingControl = formGroup.controls[passwordConfirm];
      if (control.value !== matchingControl.value) {
        matchingControl.setErrors({mustMatch: true});
      }
    };
  }

  static notEquals(password: string, value: string[]): (formGroup: FormGroup) => void {
    return (formGroup: FormGroup) => {
      const control = formGroup.controls[password];
      if (value.includes(control.value) || control.value.startsWith('pass')) {
        control.setErrors({notPassword: true});
      }
    };
  }

  static datePickerValidator(hoursNumber: number): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      let forbidden = true;
      if (control.value) {
        const now = new Date();
        const controlDate = new Date(control.value);
        const differenceMilliSeconds = controlDate.getTime() - now.getTime();
        const hours = Math.floor((differenceMilliSeconds / (1000 * 60 * 60)) % 24);
        if (hours > hoursNumber ) {
          forbidden = false;
        }
      }
      return forbidden ? {invalidDOBYear: true} : null;
    };
  }
}
