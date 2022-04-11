import {MatDialog} from "@angular/material/dialog";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {ConfirmDialogModel} from "../models/confirm/confirm-dialog.model";
import {ConfirmComponent} from "../components/confirm/confirm.component";

@Injectable({
  providedIn: 'root',
})
export class DialogService {
  constructor(private dialog: MatDialog) {}

  confirmDialog(data: ConfirmDialogModel): Observable<boolean> {
    return this.dialog
      .open(ConfirmComponent, {
        data,
        width: '400px',
        disableClose: true,
      })
      .afterClosed();
  }
}
