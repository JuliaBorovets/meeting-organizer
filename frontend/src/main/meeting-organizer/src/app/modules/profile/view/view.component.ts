import {Component, OnDestroy, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Subscription} from 'rxjs';
import {ToastrService} from 'ngx-toastr';
import {UserService} from '../../../services/user-service.service';
import {StorageService} from '../../../services/auth/storage.service';
import {UserModel} from '../../../models/user/user.model';
import {MatDialog} from '@angular/material/dialog';
import {UploadUserPhotoComponent} from '../upload-photo/upload-photo.component';

@Component({
  selector: 'app-view',
  templateUrl: './view.component.html',
  styleUrls: ['./view.component.scss']
})
export class ViewComponent implements OnInit, OnDestroy {

  updateForm: FormGroup;
  submitted = false;
  hidePasswordField = true;
  hideConfirmPasswordField = true;
  subscription: Subscription = new Subscription();
  userId: number;
  user: UserModel;

  constructor(private userService: UserService,
              private formBuilder: FormBuilder,
              public dialog: MatDialog,
              private toastrService: ToastrService,
              private storageService: StorageService) {
    this.userId = this.storageService.getUser.userId;
  }

  get f(): { [p: string]: AbstractControl } {
    return this.updateForm.controls;
  }

  ngOnInit(): void {
    this.createUpdateForm();
    this.initForm();
  }

  initForm(): void {
    this.subscription.add(
      this.userService.getUserById(this.userId).subscribe(
        data => {
          this.user = data;
          this.updateForm.setValue(data);
          this.updateForm.controls.password.setValue('');
        },
        () => this.showError())
    );
  }

  createUpdateForm(): void {
    this.updateForm = this.formBuilder.group({
      userId: [],
      roles: [],
      email: ['', Validators.compose([
        Validators.email,
        Validators.maxLength(128)])
      ],
      username: ['', Validators.compose([
        Validators.minLength(4),
        Validators.maxLength(20),
        Validators.pattern('[A-Za-z0-9]*')])
      ],
      firstName: ['', Validators.compose([
        Validators.minLength(1),
        Validators.maxLength(50)])
      ],
      lastName: ['', Validators.compose([
        Validators.minLength(1),
        Validators.maxLength(50)])
      ],
      password: ['', Validators.compose([
        Validators.minLength(8),
        Validators.maxLength(30),
        Validators.pattern('[A-Za-z0-9]*')])
      ],
      imagePath: null
    });
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.updateForm.invalid) {
      return;
    }

    const updateRequest = {
      userId: this.userId,
      email: this.f.email.value,
      username: this.f.username.value,
      password: this.f.password.value,
      firstName: this.f.firstName.value,
      lastName: this.f.lastName.value
    };

    this.subscription.add(
      this.userService.updateUser(updateRequest)
        .subscribe(
          () => this.showSuccess(),
          () => this.showError()
        )
    );
  }

  public showSuccess(): void {
    this.toastrService.success('Success!', 'Updated!');
  }

  public showError(): void {
    this.toastrService.error('Error!', 'Update failed!');
  }

  public cancelUpdating() {
    this.updateForm.reset();
    this.initForm();
  }


  showChangePhoto() {
    const uploadDialogRef = this.dialog.open(UploadUserPhotoComponent, {
      height: 'auto',
      width: '80vh',
      data: {
        userId: this.userId
      }
    });

    this.subscription.add(
      uploadDialogRef.afterClosed().subscribe(
        () => this.initForm()
      ));
  }
  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

}
