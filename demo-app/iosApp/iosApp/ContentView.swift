//
//  ContentView.swift
//  iosApp
//
//  Created by Salomon BRYS on 19/02/2021.
//

import SwiftUI
import shared

struct ContentView: View {
    @State var encoded = ""
    
    var body: some View {
        Button {
            self.encoded = DemoController().encrypt(nonce: 42, text: "Hello, World!")
        } label: {
            Text("Click me!")
        }
            .padding()
        Text(encoded)
            .padding()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
