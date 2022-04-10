import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Subscription} from 'rxjs';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthService} from '../../../../../services/auth/auth.service';
import {StorageService} from '../../../../../services/auth/storage.service';
import {ToastrService} from 'ngx-toastr';
import {CustomValidator} from '../../../../../services/custom-validator.service';
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.scss']
})
export class CreateComponent implements OnInit, OnDestroy {

  recoveryForm: FormGroup;
  submitted = false;
  hideNewPasswordField = true;
  hideConfirmPasswordField = true;
  hide = true;
  token = '';
  email = '';

  subscription: Subscription = new Subscription();

  constructor(private route: ActivatedRoute,
              private authService: AuthService,
              private router: Router,
              private storageService: StorageService,
              private formBuilder: FormBuilder,
              private toastrService: ToastrService,
              public dialogRef: MatDialogRef<CreateComponent>) {
    this.route.queryParams.subscribe(params => this.token = params[`token`]);
  }

  ngOnInit(): void {
    this.subscription.add(
      this.authService.confirmResetPassword(this.token).subscribe(
        email => {
          if (email == null || email.length === 0) {
            this.showError();
          } else {
            this.email = email;
          }
        },
        () => this.showError()
      )
    );

    this.recoveryForm = this.formBuilder.group({
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

  get f() {
    return this.recoveryForm.controls;
  }


  onSubmit(): void {
    this.submitted = true;

    if (this.recoveryForm.invalid) {
      return;
    }

    const recoveryResponse = {
      email: this.email,
      password: this.f.password.value
    };

    this.subscription.add(
      this.authService.resetPassword(recoveryResponse.email, recoveryResponse.password).subscribe(
        () => {
          this.showSuccess();
          this.closeDialog();
        },
        () => this.showError()
      )
    );
  }

  public showSuccess(): void {
    this.toastrService.success('Success!', 'Success!');
  }

  public showError(): void {
    this.toastrService.error('Error!', 'Error!');
  }

  closeDialog() {
    this.dialogRef.close();
  }

  ngOnDestroy() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

}
