/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { HopeJustFoundTestModule } from '../../../test.module';
import { DonationRequestComponent } from 'app/entities/donation-request/donation-request.component';
import { DonationRequestService } from 'app/entities/donation-request/donation-request.service';
import { DonationRequest } from 'app/shared/model/donation-request.model';

describe('Component Tests', () => {
    describe('DonationRequest Management Component', () => {
        let comp: DonationRequestComponent;
        let fixture: ComponentFixture<DonationRequestComponent>;
        let service: DonationRequestService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HopeJustFoundTestModule],
                declarations: [DonationRequestComponent],
                providers: []
            })
                .overrideTemplate(DonationRequestComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DonationRequestComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DonationRequestService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new DonationRequest(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.donationRequests[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
