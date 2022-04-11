import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';
import {ConfirmDialogModel} from '../../models/confirm/confirm-dialog.model';

@Component({
  selector: 'app-confirm',
  templateUrl: './confirm.component.html',
  styleUrls: ['./confirm.component.scss']
})
export class ConfirmComponent implements OnInit {
  constructor(@Inject(MAT_DIALOG_DATA) public data: ConfirmDialogModel) {}

  ngOnInit(): void {}
}
