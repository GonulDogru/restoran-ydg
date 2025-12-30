
package com.restoran.state;

public interface MasaState {
    void masaAc(int masaId);
    void siparisAl(int masaId);
    void odemeAl(int masaId);
    void rezervasyonYap(int masaId);
    String getDurumAdi();
}