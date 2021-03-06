import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Donation } from 'app/shared/model/donation.model';
import { DonationService } from './donation.service';
import { DonationComponent } from './donation.component';
import { DonationDetailComponent } from './donation-detail.component';
import { DonationUpdateComponent } from './donation-update.component';
import { DonationDeletePopupComponent } from './donation-delete-dialog.component';
import { IDonation } from 'app/shared/model/donation.model';

@Injectable({ providedIn: 'root' })
export class DonationResolve implements Resolve<IDonation> {
    constructor(private service: DonationService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Donation> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Donation>) => response.ok),
                map((donation: HttpResponse<Donation>) => donation.body)
            );
        }
        return of(new Donation());
    }
}

export const donationRoute: Routes = [
    {
        path: 'donation',
        component: DonationComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Donations'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'donation/:id/view',
        component: DonationDetailComponent,
        resolve: {
            donation: DonationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Donations'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'donation/new',
        component: DonationUpdateComponent,
        resolve: {
            donation: DonationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Donations'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'donation/:id/edit',
        component: DonationUpdateComponent,
        resolve: {
            donation: DonationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Donations'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const donationPopupRoute: Routes = [
    {
        path: 'donation/:id/delete',
        component: DonationDeletePopupComponent,
        resolve: {
            donation: DonationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Donations'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
