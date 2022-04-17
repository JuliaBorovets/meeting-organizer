import {Component, Inject, OnDestroy, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material/dialog';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Subscription} from 'rxjs';
import {StreamService} from '../../../../services/stream/stream.service';

@Component({
  selector: 'app-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.scss']
})
export class CreateStreamComponent implements OnInit, OnDestroy {

  createForm: FormGroup;
  isCreateFailed = false;
  errorMessage = '';
  submitted = false;
  libraryId: number;
  isError = false;
  isInValid = false;
  private subscription: Subscription;

  constructor(public dialogRef: MatDialogRef<CreateStreamComponent>,
              private formBuilder: FormBuilder,
              public dialog: MatDialog,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private streamService: StreamService) {
    this.libraryId = data.libraryId;
    this.subscription = new Subscription();
  }

  ngOnInit(): void {
    this.createCreateForm();
  }

  createCreateForm(): void {
    this.createForm = this.formBuilder.group({
      name: [null, Validators.compose([
        Validators.required,
        Validators.minLength(4),
        Validators.maxLength(50)])
      ]
    });
  }

  get f() {
    return this.createForm.controls;
  }

  onSubmit(): void {
    this.submitted = true;
    if (this.createForm.invalid) {
      return;
    }

    const createRequest = {
      name: this.f.name.value,
      libraryId: this.libraryId
    };

    this.subscription = this.streamService.create(createRequest).subscribe(
      () => {
        this.dialogRef.close();
      },
      () => {
        this.isError = true;
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
