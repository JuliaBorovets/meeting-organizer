import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {CommentModel} from '../../../models/reaction/comment.model';
import {DialogService} from '../../../services/confirm-dialog.service';
import {StorageService} from '../../../services/auth/storage.service';
import {ReactionService} from '../../../services/reaction/reaction.service';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.scss']
})
export class CommentComponent implements OnInit {

  @Input() comment: CommentModel;
  @Output() updateEvent = new EventEmitter();
  userId: number;

  constructor(private dialogService: DialogService,
              private storageService: StorageService,
              private reactionService: ReactionService,
              private toastrService: ToastrService) {
    this.userId = this.storageService.getUser.userId;
  }

  ngOnInit(): void {
  }

  deleteComment(): void {
    this.dialogService.confirmDialog({
      title: 'Confirm deletion',
      message: 'Do you want to confirm this action?',
      confirmCaption: 'Confirm',
      cancelCaption: 'Cancel',
    })
      .subscribe((confirmed) => {
        if (confirmed) {
          this.reactionService.deleteComment(this.comment.commentId).subscribe(
            () => {
              this.toastrService.success('Success!', 'Deleted!');
              this.updateEvent.emit();
            },
            () => this.toastrService.error('Error!', 'Deletion failed!')
          );
        }
      });
  }

  isCommentOwner(): boolean {
    return this.comment.userId === this.userId;
  }
}
