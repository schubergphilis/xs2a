import { TestBed, inject, async } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { PisService } from './pis.service';

describe('PisService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [PisService]
    });
  });

  it('should be created', async(inject([HttpTestingController, PisService], (httpClient: HttpTestingController, service: PisService) => {
    expect(service).toBeTruthy();
  })));
});
