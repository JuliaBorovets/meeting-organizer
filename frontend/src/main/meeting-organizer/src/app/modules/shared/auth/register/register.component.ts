import {Component, OnDestroy, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Subscription} from 'rxjs';
import {AuthService} from '../../../../services/auth/auth.service';
import {Router} from '@angular/router';
import {CustomValidator} from '../../../../services/custom-validator.service';
import {MatDialogRef} from '@angular/material/dialog';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit, OnDestroy {

  registrationForm: FormGroup;
  isSuccessful = false;
  isError = false;
  isLoading = true;
  errorMessage = '';
  submitted = false;
  hidePasswordField = true;
  hideConfirmPasswordField = true;
  private subscription: Subscription = new Subscription();

  constructor(public dialogRef: MatDialogRef<RegisterComponent>,
              private authService: AuthService,
              private router: Router,
              private formBuilder: FormBuilder,
              private toastrService: ToastrService) {
  }

  get f(): { [p: string]: AbstractControl } {
    return this.registrationForm.controls;
  }

  ngOnInit(): void {
    this.createRegistrationForm();
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.registrationForm.invalid) {
      return;
    }

    const registrationResponse = {
      email: this.f.email.value,
      username: this.f.username.value,
      password: this.f.password.value,
      firstName: this.f.firstName.value,
      lastName: this.f.lastName.value
    };

    this.subscription.add(
      this.authService.register(registrationResponse)
        .subscribe(
          data => {
            this.isSuccessful = true;
            this.showSuccess();
            this.dialogRef.close();
          },
          err => {
            this.errorMessage = err.error.message;
            this.isError = true;
            this.showError();
          }
        ));
  }

  createRegistrationForm(): void {
    this.registrationForm = this.formBuilder.group({
      email: [null, Validators.compose([
        Validators.email,
        Validators.maxLength(128)])
      ],
      username: [null, Validators.compose([
        Validators.minLength(4),
        Validators.maxLength(20),
        Validators.pattern('[A-Za-z0-9]*')])
      ],
      firstName: [null, Validators.compose([
        Validators.minLength(1),
        Validators.maxLength(50)])
      ],
      lastName: [null, Validators.compose([
        Validators.minLength(1),
        Validators.maxLength(50)])
      ],
      password: [null, Validators.compose([
        Validators.minLength(8),
        Validators.maxLength(30),
        Validators.pattern('[A-Za-z0-9]*')])
      ],
      passwordConfirm: [null, Validators.compose([Validators.required])]
    }, {
      validator: CustomValidator.passwordMatchValidator('password', 'passwordConfirm')
    });
  }

  public showSuccess(): void {
    this.toastrService.success('Success!', 'Check your email!');
  }

  public showError(): void {
    this.toastrService.error('Error!', 'Registration failed!');
  }

  closeDialog() {
    this.dialogRef.close();
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
