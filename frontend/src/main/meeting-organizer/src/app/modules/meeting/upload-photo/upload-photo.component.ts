import {Component, ElementRef, Inject, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Subscription} from 'rxjs';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material/dialog';
import {ToastrService} from 'ngx-toastr';
import {EventService} from '../../../services/event/event.service';

@Component({
  selector: 'app-upload-photo',
  templateUrl: './upload-photo.component.html',
  styleUrls: ['./upload-photo.component.scss']
})
export class UploadPhotoComponent implements OnInit, OnDestroy {

  @ViewChild('fileInput') fileInput: ElementRef;
  fileAttr = 'Choose File';
  private subscription: Subscription;
  file: any;
  eventId: number;

  constructor(public dialogRef: MatDialogRef<UploadPhotoComponent>,
              public dialog: MatDialog,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private eventService: EventService,
              private toastrService: ToastrService) {
    this.eventId = data.eventId;
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

    this.subscription = this.eventService.uploadEventImage(this.eventId, formData).subscribe(
      () => {
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
