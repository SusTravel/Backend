package org.sustrav.demo.utils;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.hibernate.engine.jdbc.BlobProxy;
import org.sustrav.demo.data.model.Place;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;

public class QRCodeHandler {

    public static Blob generateCodeAsBlob(Place place) {
        return BlobProxy.generateProxy(QRCodeHandler.generateCode(place).toByteArray());
    }

    public static ByteArrayOutputStream generateCode(Place place) {
        ByteArrayOutputStream bout =
                QRCode.from("https://sustrav.org/" + place.getId())
                        .withSize(250, 250)
                        .to(ImageType.PNG)
                        .stream();
        return bout;
    }
}
