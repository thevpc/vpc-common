/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.iconset.util;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.net.URL;
import javax.imageio.ImageIO;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;

/**
 *
 * @author vpc
 */
public class SVGBatik {

//    public static void main(String[] args) {
//        URL url;
//        try {
//            url = new URL("file:/data/git/more-iconsets/iconset-feather/src/main/resources/net/thevpc/more/iconsets/feather/airplay.svg");
//        } catch (MalformedURLException ex) {
//            Logger.getLogger(SVGBatik.class.getName()).log(Level.SEVERE, null, ex);
//            return;
//        }
//        Image m = getImageFromSvg(url, 1024);
//        ColorIconTransform c = new ColorIconTransform(Color.BLACK, Color.RED);
//        m = c.transformIcon(m);
//
//        JLabel lab = new JLabel(
//                new ImageIcon(
//                        m)
//        );
//        JOptionPane.showMessageDialog(null, lab);
//    }

    public static Image getImageFromSvg(URL url, int width0) {
        try {
            PNGTranscoder t = new PNGTranscoder();
            TranscoderInput input = new TranscoderInput(url.toURI().toString());
            ByteArrayOutputStream ostream = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(ostream);
            t.addTranscodingHint(JPEGTranscoder.KEY_WIDTH, new Float(width0));
            t.transcode(input, output);
            ByteArrayInputStream bis = new ByteArrayInputStream(ostream.toByteArray());
            return ImageIO.read(bis);
//            return ImageIO.read(new ) // Load SVG resource into a document
//            String parser = XMLResourceDescriptor.getXMLParserClassName();
//            SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
//            SVGDocument document = (SVGDocument) f.createDocument(url.toString());
//
//// Build the tree and get the document dimensions
//            UserAgentAdapter userAgentAdapter = new UserAgentAdapter();
//            BridgeContext bridgeContext = new BridgeContext(userAgentAdapter);
//            GVTBuilder builder = new GVTBuilder();
//            GraphicsNode graphicsNode = builder.build(bridgeContext,
//                    document);
//
//            final SVGSVGElement rootElement = document.getRootElement();
//            rootElement.setAttribute("viewBox", "0 0 400 400");
//            final SVGLength width = rootElement.getWidth().getBaseVal();
//            final SVGLength height = rootElement.getHeight().getBaseVal();
//            float defaultWidth = (float) Math.ceil(width.getValue());
//            float defaultHeigth = (float) Math.ceil(height.getValue());
//            if (defaultWidth == 1f && defaultHeigth == 1f) {
//                defaultWidth = 200;
//                defaultHeigth = 200;
//            }
//// Paint svg into image buffer
//            BufferedImage bufferedImage = new BufferedImage((int) defaultWidth,
//                    (int) defaultHeigth, BufferedImage.TYPE_INT_ARGB);
//            Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();
//            graphicsNode.paint(g2d);
//            g2d.drawRect(0, 0, (int) defaultWidth, (int) defaultHeigth);
//
//// Cleanup and return image
//            g2d.dispose();
//            return bufferedImage;
        } catch (TranscoderException | URISyntaxException ex) {
            throw new UncheckedIOException(new IOException(ex));
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
