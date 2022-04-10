import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from 'rxjs';
import {TokenVerifyService} from '../../../../services/auth/token-verify.service';
import {ActivatedRoute, Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-registration-verify',
  templateUrl: './registration-verify.component.html',
  styleUrls: ['./registration-verify.component.scss']
})
export class RegistrationVerifyComponent implements OnInit, OnDestroy {

  subscription: Subscription = new Subscription();
  token: string;

  constructor(private tokenVerifyService: TokenVerifyService,
              private route: ActivatedRoute,
              private router: Router,
              private toastrService: ToastrService) {
    this.route.queryParams.subscribe(params => this.token = params[`token`]);
  }

  ngOnInit(): void {
    this.subscription.add(
      this.tokenVerifyService.confirmRegistration(this.token).subscribe(
        () => this.showSuccess(),
        () => this.showError()
      )
    );
  }

  public showSuccess(): void {
    this.toastrService.success('Success!', 'Registration is verified!');
  }

  public showError(): void {
    this.toastrService.error('Error!', 'Registration is not verified!');
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
