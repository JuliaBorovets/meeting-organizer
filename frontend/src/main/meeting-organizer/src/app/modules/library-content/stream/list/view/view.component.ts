import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {StreamModel} from '../../../../../models/stream/stream.model';
import {MatDialog} from '@angular/material/dialog';
import {Subscription} from 'rxjs';
import {DialogService} from '../../../../../services/confirm-dialog.service';
import {StreamService} from '../../../../../services/stream/stream.service';
import {ToastrService} from 'ngx-toastr';
import {UpdateStreamComponent} from '../../update/update-stream.component';
import {AddEventComponent} from '../../add-event/add-event.component';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-stream-view',
  templateUrl: './view.component.html',
  styleUrls: ['./view.component.scss']
})
export class ViewComponent implements OnInit, OnDestroy {

  @Input() streamModel: StreamModel;
  @Output() updateStreamEvent = new EventEmitter();

  private subscription: Subscription = new Subscription();
  public isError = false;
  libraryId: number;

  constructor(public dialog: MatDialog,
              private dialogService: DialogService,
              private streamService: StreamService,
              private toastrService: ToastrService,
              private route: ActivatedRoute,
              private router: Router) {
    this.route.params.subscribe(params => {
      this.libraryId = +params.libraryId;
    });
  }

  ngOnInit(): void {
  }

  openUpdateDialog(): void {
    const updateDialogRef = this.dialog.open(UpdateStreamComponent, {
      height: 'auto',
      width: '65vh',
      data: {
        streamModel: this.streamModel,
        libraryId: this.libraryId
      }
    });

    this.subscription.add(
      updateDialogRef.afterClosed().subscribe(
        () => this.updateStreamEvent.emit(),
        () => this.isError = true)
    );
  }

  openAddEventDialog(): void {
    const addEventDialogRef = this.dialog.open(AddEventComponent, {
      height: '40vh',
      width: '80vh',
      data: {streamModel: this.streamModel}
    });

    this.subscription.add(
      addEventDialogRef.afterClosed().subscribe(
        () => this.updateStreamEvent.emit(),
        () => this.isError = true)
    );
  }

  deleteStream(): void {
    this.dialogService.confirmDialog({
      title: 'Confirm deletion',
      message: 'Do you want to confirm this action?',
      confirmCaption: 'Confirm',
      cancelCaption: 'Cancel',
    })
      .subscribe((confirmed) => {
        if (confirmed) {
          this.streamService.deleteStream(this.streamModel.streamId).subscribe(
            () => {
              this.toastrService.success('Success!', 'Deleted!');
              this.updateStreamEvent.emit();
            },
            () => this.toastrService.error('Error!', 'Deletion failed!')
          );
        }
      });
  }

  openStreamContent(): void {
    this.router.navigate(['library-content/' + this.streamModel.libraryId + '/stream/view/' + this.streamModel.streamId]).then();
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
