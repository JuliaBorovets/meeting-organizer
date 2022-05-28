import {Component, ElementRef, Inject, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Subscription} from 'rxjs';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material/dialog';
import {ToastrService} from 'ngx-toastr';
import {UserService} from '../../../services/user-service.service';
import {StorageService} from "../../../services/auth/storage.service";
import {UserModel} from "../../../models/user/user.model";

@Component({
  selector: 'app-upload-photo',
  templateUrl: './upload-photo.component.html',
  styleUrls: ['./upload-photo.component.scss']
})
export class UploadUserPhotoComponent implements OnInit, OnDestroy {

  @ViewChild('fileInput') fileInput: ElementRef;
  fileAttr = 'Choose File';
  private subscription: Subscription;
  file: any;
  userId: number;

  constructor(public dialogRef: MatDialogRef<UploadUserPhotoComponent>,
              public dialog: MatDialog,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private userService: UserService,
              private storageService: StorageService,
              private toastrService: ToastrService) {
    this.userId = data.userId;
  }

  ngOnInit(): void {
  }

  uploadFileEvt(imgFile: any) {
    if (imgFile.target.files && imgFile.target.files[0]) {
      this.fileAttr = imgFile.target.files[0].name;
      this.file = imgFile.target.files[0];
    } else {
      this.fileAttr = 'Choose File';
    }
  }

  onSubmit(): void {
    if (!this.file) {
      return;
    }

    const formData = new FormData();

    formData.append('image', this.file);

    this.subscription = this.userService.uploadUserImage(this.userId, formData).subscribe(
      (data) => {
        const userFromStorage = this.storageService.getUser;
        const user: UserModel = JSON.parse(data.response);
        userFromStorage.imagePath = user.imagePath;
        this.storageService.setUser(userFromStorage);
        this.toastrService.success('Success!', 'Uploaded!');
        this.dialogRef.close();
      },
      () => {
        this.toastrService.error('Error!', 'Failed to upload!');
      }
    );
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
