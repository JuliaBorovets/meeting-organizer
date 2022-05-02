import {Component, OnDestroy, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Subscription} from 'rxjs';
import {MatDialogRef} from '@angular/material/dialog';
import {ToastrService} from 'ngx-toastr';
import {ContactUsService} from '../../../services/contact-us.service';

@Component({
  selector: 'app-contact-us',
  templateUrl: './contact-us.component.html',
  styleUrls: ['./contact-us.component.scss']
})
export class ContactUsComponent implements OnInit, OnDestroy {

  public contactUsForm: FormGroup;
  public submitted = false;
  public isError = false;
  private readonly subscription: Subscription;

  constructor(private formBuilder: FormBuilder,
              private contactUsService: ContactUsService,
              private dialogRef: MatDialogRef<ContactUsComponent>,
              private toastrService: ToastrService) {
    this.subscription = new Subscription();
  }

  ngOnInit(): void {
    this.creatContactUsForm();
  }

  creatContactUsForm(): void {
    this.contactUsForm = this.formBuilder.group({
      name: ['', null],
      email: ['', Validators.compose([
        Validators.required,
        Validators.email])
      ],
      message: ['', null]
    });
  }

  get f(): { [p: string]: AbstractControl } {
    return this.contactUsForm.controls;
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.contactUsForm.invalid) {
      return;
    }

    const contactUsRequest = {
      name: this.f.name.value,
      email: this.f.email.value,
      message: this.f.message.value
    };

    this.subscription.add(
      this.contactUsService.create(contactUsRequest)
        .subscribe(
          () => {
            this.dialogRef.close();
            this.toastrService.success('Success!', 'Success!');
          },
          () => {
            this.isError = true;
            this.toastrService.error('Error!', 'Failed!');
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
