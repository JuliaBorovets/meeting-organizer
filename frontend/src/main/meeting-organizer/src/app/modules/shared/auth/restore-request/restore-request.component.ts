import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Subscription} from 'rxjs';
import {AuthService} from '../../../../services/auth/auth.service';
import {Router} from '@angular/router';
import {StorageService} from '../../../../services/auth/storage.service';
import {MatDialogRef} from '@angular/material/dialog';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-restore-request',
  templateUrl: './restore-request.component.html',
  styleUrls: ['./restore-request.component.scss']
})
export class RestoreRequestComponent implements OnInit, OnDestroy {

  recoveryForm: FormGroup;
  submitted = false;
  notFound = false;

  subscription: Subscription = new Subscription();

  constructor(public dialogRef: MatDialogRef<RestoreRequestComponent>,
              private authService: AuthService,
              private router: Router,
              private storageService: StorageService,
              private formBuilder: FormBuilder,
              private toastrService: ToastrService
  ) {
  }

  ngOnInit(): void {
    this.recoveryForm = this.formBuilder.group({
      email: [null, [Validators.required]]
    });
  }

  get f() {
    return this.recoveryForm.controls;
  }

  onSubmit(): void {
    this.notFound = false;
    this.submitted = true;

    if (this.recoveryForm.invalid) {
      return;
    }

    const recoveryResponse = {
      email: this.f.email.value
    };

    this.subscription.add(this.authService.forgotPassword(recoveryResponse.email).subscribe(
        () => {
          this.showSuccess();
          this.closeDialog();
        },
        () => {
          this.notFound = true;
          this.showError();
        }
      )
    );
  }

  public showSuccess(): void {
    this.toastrService.success('Success!', 'Check your email!');
  }

  public showError(): void {
    this.toastrService.error('Error!', 'Cannot process the request!');
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
