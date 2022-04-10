import {Component, OnDestroy, OnInit} from '@angular/core';
import {StorageService} from './services/auth/storage.service';
import {UserModel} from './models/user/user.model';
import {Subscription} from 'rxjs';
import {Router} from '@angular/router';
import {AuthService} from './services/auth/auth.service';
import {updateWhile} from "@schematics/angular/third_party/github.com/Microsoft/TypeScript/lib/typescript";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnDestroy, OnInit {
  title = 'Meeting organizer';
  isMenuOpen = false;
  user: UserModel;
  subscription: Subscription = new Subscription();

  links = [
    {
      link: 'projects-menu',
      label: 'Projects'
    },
    {
      link: 'libraries-menu',
      label: 'Libraries'
    }
  ];
  activeLink = this.links[0];

  constructor(private storageService: StorageService,
              private router: Router,
              private authService: AuthService) {
  }

  ngOnInit(): void {
    this.subscription.add(
      this.storageService.currentUser.subscribe(
        user => {
          this.user = user;
          console.log('--', user);
        }
      )
    );
  }


  logout(): void {
    this.authService.logout();
    this.router.navigateByUrl('auth/welcome').then();
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
