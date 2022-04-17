import {Component, Inject, OnDestroy, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Subscription} from 'rxjs';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {ToastrService} from 'ngx-toastr';
import {StreamModel} from '../../../../models/stream/stream.model';
import {StreamService} from '../../../../services/stream/stream.service';

@Component({
  selector: 'app-update',
  templateUrl: './update-stream.component.html',
  styleUrls: ['./update-stream.component.scss']
})
export class UpdateStreamComponent implements OnInit, OnDestroy {

  public streamUpdateForm: FormGroup;
  public submitted = false;
  public isError = false;
  streamItem: StreamModel;
  private readonly subscription: Subscription;

  constructor(private formBuilder: FormBuilder,
              private streamService: StreamService,
              private dialogRef: MatDialogRef<UpdateStreamComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private toastrService: ToastrService) {
    this.streamItem = data.streamModel;
    this.streamItem.libraryId = data.libraryId;
    this.subscription = new Subscription();
  }

  ngOnInit(): void {
    this.createStreamUpdateForm();
    this.streamUpdateForm.setValue(this.streamItem);
  }

  get f(): { [p: string]: AbstractControl } {
    return this.streamUpdateForm.controls;
  }

  createStreamUpdateForm(): void {
    this.streamUpdateForm = this.formBuilder.group({
      streamId: null,
      libraryId: null,
      name: ['', Validators.compose([
        Validators.required,
        Validators.minLength(4),
        Validators.maxLength(50)])
      ]
    });
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.streamUpdateForm.invalid) {
      return;
    }

    const streamUpdateRequest = {
      streamId: this.streamItem.streamId,
      libraryId: this.streamItem.libraryId,
      name: this.f.name.value
    };

    this.subscription.add(
      this.streamService.updateStream(streamUpdateRequest)
        .subscribe(
          () => {
            this.dialogRef.close();
            this.toastrService.success('Success!', 'Updated!');
          },
          () => {
            this.isError = true;
            this.toastrService.error('Error!', 'Update failed!');
          }
        )
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
